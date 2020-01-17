#pragma once
#include <IShotManager.h>
#include <IRremote.h>
#include <BolgConfig.h>
#include <IPlayer.h>
#include <memory>
#include <sstream>
#include <ICommandSender.h>

namespace bolg
{
    /// 発砲
    class Shot : public IShot
    {
    private:

        std::shared_ptr<IPlayer> m_player;

    public:

        Shot(std::shared_ptr<IPlayer> player) : m_player(player){}

        ShotData getShotData() override
        {
            return {static_cast<uint32_t>(m_player->getUserID()),sizeof(m_player->getUserID())*8};
        }
    };

    /// 発砲管理
    class ShotManager : public IShotManager
    {
    private:

        IRsend m_irSend;

        std::shared_ptr<ICommandSender> m_commandSender;

        std::shared_ptr<IRreceiveTask> m_irreceiveTask;

    public:

        ShotManager(std::shared_ptr<ICommandSender> commandSender,std::shared_ptr<IRreceiveTask> irreceiveTask):
            m_irSend(BOLG_IR_SEND_PIN),
            m_commandSender(commandSender),
            m_irreceiveTask(irreceiveTask){}

        void createShot(std::shared_ptr<IShot>& shot)
        {
            const auto shotData = shot->getShotData();

            m_irreceiveTask->finalize();

            m_irSend.sendSony(shotData.data, shotData.bits);

            m_irreceiveTask->init();

            m_commandSender->addCommand(SendCommand::Shot);
        }
    };
}