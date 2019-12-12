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

namespace bolg
{
    enum class GAME_STATE
    {
        START,
        STANDBY,
        PAUSE
    };

    class GameManager
    {
    private:

        std::shared_ptr<BluetoothSerial> m_spp;
        std::shared_ptr<IRreceiveTask> m_irReceiveTask;
        std::shared_ptr<CommandSendTask> m_commandSendTask;
        std::shared_ptr<CommandReveiveTask> m_commandReceiveTask;
        std::shared_ptr<TriggerTask> m_triggerTask;

        std::shared_ptr<Player> m_player;
        std::shared_ptr<IGun> m_gun;

    public:

        GameManager()
        {
            pinMode(BUILTIN_LED,OUTPUT);
            Serial.begin(115200);

            m_spp = std::make_shared<BluetoothSerial>();

            m_spp->begin(BOLG_BLUETOOTH_DEVICE_NAME);

            while(!m_spp->hasClient())
            {
                delay(1);
            }

            m_commandSendTask    = std::make_shared<CommandSendTask>(m_spp);
            m_commandReceiveTask = std::make_shared<CommandReveiveTask>(m_spp);

            m_player = std::make_shared<Player>();
            m_gun    = std::make_shared<Gun>(m_player,std::make_shared<ShotManager>(m_commandSendTask),std::make_shared<Shot>(m_player));

            m_player->init(0x00, m_gun);

            m_irReceiveTask      = std::make_shared<IRreceiveTask>(std::make_shared<Damage>(m_player, m_commandSendTask));
            m_triggerTask        = std::make_shared<TriggerTask>(m_player);

            auto player = m_player;
            auto gun    = m_gun;


            auto irReceiveTask = m_irReceiveTask;
            auto triggerTask = m_triggerTask;

            // タスク達を起動
            m_commandSendTask->create();
            m_commandReceiveTask->create();
            m_irReceiveTask->create();
            m_triggerTask->create();

            // userIDセット
            m_commandReceiveTask->setCommandListener(static_cast<uint8_t>(ReceiveCommand::SET_USER_ID), [player,gun](std::vector<uint8_t>& args)
            {
                int32_t userID = 0x00;

                ::memcpy(&userID, &args[0], sizeof(int32_t));

                player->init(userID, gun);
            },4);

            // GameState変更
            m_commandReceiveTask->setCommandListener(static_cast<uint8_t>(ReceiveCommand::SET_GAME_STATE), [irReceiveTask,triggerTask](std::vector<uint8_t>& args)
            {
                GAME_STATE state = static_cast<GAME_STATE>(args[0]);

                switch (state)
                {
                case GAME_STATE::START:
                    irReceiveTask->create();
                    triggerTask->create();
                    break;
                case GAME_STATE::PAUSE:
                case GAME_STATE::STANDBY:
                    irReceiveTask->destroy();
                    triggerTask->destroy();
                    break;
                default:
                    break;
                }
            }, 1);
        }

        void update()
        {
            // Bluetoothが切断されているときはタイマーが競合するので赤外線受信を止める
            if(!m_spp->hasClient())
            {
                digitalWrite(BUILTIN_LED,LOW);
                m_irReceiveTask->finalize();
            }
            else
            {
                digitalWrite(BUILTIN_LED,HIGH);
                m_irReceiveTask->init();
            }
        }
    };
}