#pragma once
#include <memory>
#include "Tasks/CommandReceiveTask.h"
#include "Tasks/CommandSendTask.h"
#include "Tasks/IRreceiveTask.h"
#include "Tasks/IRsendTask.h"
#include "Tasks/TriggerTask.h"
#include <BluetoothSerial.h>
#include <Player.h>
#include "ShotManager.h"
#include <esp_log.h>
#include <BoLGLogger.h>

namespace bolg
{
    enum class GAME_STATE
    {
        START,
        STANDBY,
        PAUSE
    };

    /// ゲーム管理
    class GameManager
    {
    private:

        // Tasks
        std::shared_ptr<BluetoothSerial>    m_spp;
        std::shared_ptr<IRreceiveTask>      m_irReceiveTask;
        std::shared_ptr<CommandSendTask>    m_commandSendTask;
        std::shared_ptr<CommandReveiveTask> m_commandReceiveTask;
        std::shared_ptr<TriggerTask>        m_triggerTask;

        // others
        std::shared_ptr<Player> m_player;
        std::shared_ptr<IGun>   m_gun;

    public:

        GameManager()
        {
            Serial.begin(115200);

            BOLG_LOG("==========================================\n");
            BOLG_LOG("---------- BoLG_VERSION %s ----------\n",BOLG_VERSION);
            BOLG_LOG("==========================================\n\n");

            BOLG_LOG("========== Application init start ==========\n");

            pinMode(BOLG_VIBRATION_PIN,OUTPUT);

            m_spp = std::make_shared<BluetoothSerial>();

            m_spp->begin(BOLG_BLUETOOTH_DEVICE_NAME);

            m_commandSendTask    = std::make_shared<CommandSendTask>(m_spp);
            m_commandReceiveTask = std::make_shared<CommandReveiveTask>(m_spp);
            m_player             = std::make_shared<Player>();
            m_irReceiveTask      = std::make_shared<IRreceiveTask>(std::make_shared<Damage>(m_player,m_player, m_commandSendTask));
            m_triggerTask        = std::make_shared<TriggerTask>(m_player);
            m_gun                = std::make_shared<Gun>(m_player,std::make_shared<ShotManager>(m_commandSendTask,m_irReceiveTask),std::make_shared<Shot>(m_player));

            m_player->init(BOLG_INIT_USER_ID, m_gun);

            auto player = m_player;
            auto gun    = m_gun;

            // タスク達を起動
            m_commandSendTask->create(0,0);
            m_commandReceiveTask->create(0,0);
            m_irReceiveTask->create(1,0);
            m_triggerTask->create(1,0);

            // userIDセット
            m_commandReceiveTask->setCommandListener(static_cast<uint8_t>(ReceiveCommand::SET_USER_ID), [player, gun](std::vector<uint8_t>& args)
            {
                int32_t userID = BOLG_INIT_USER_ID;

                ::memcpy(&userID, &args[0], sizeof(int32_t));

                BOLG_LOG("SET_USER_ID id : %d\n", userID);

                player->init(userID, gun);
            },4);

            BOLG_LOG("Init UserID : %d\n",BOLG_INIT_USER_ID);
            BOLG_LOG("Bluetooth PowerON\n");
            BOLG_LOG("Bluetooth DeviceName  %s\n", BOLG_BLUETOOTH_DEVICE_NAME);
            BOLG_LOG("========== Application init complete ==========\n\n");
            BOLG_LOG("Bluetooth Connect Waiting...\n");
        }

        ///
        /// @brief   更新処理
        ///
        void update()
        {
            static bool prevBluetoothEnable = m_spp->hasClient();
            bool bluetoothEnable = m_spp->hasClient();

            // Bluetoothが切断されているときはタイマーが競合するので赤外線受信を止める
            if((!bluetoothEnable) && prevBluetoothEnable)
            {
                m_irReceiveTask->finalize();
                BOLG_LOG("Bluetooth DisConnected\n");
            }
            else if(bluetoothEnable && (!prevBluetoothEnable))
            {
                m_irReceiveTask->init();
                BOLG_LOG("Bluetooth Connected\n");
            }

            prevBluetoothEnable = bluetoothEnable;
        }
    };
}