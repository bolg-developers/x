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

    class ShotManager : public IShotManager
    {
    private:

        IRsend m_irSend;

        std::shared_ptr<ICommandSender> m_commandSender;

    public:

        ShotManager(std::shared_ptr<ICommandSender> commandSender):m_irSend(BOLG_IR_SEND_PIN),m_commandSender(commandSender){}

        void createShot(std::shared_ptr<IShot> shot)
        {
            auto shotData = shot->getShotData();

            m_irSend.sendSony(shotData.data, shotData.bits);

            m_commandSender->addCommand(SendCommand::Shot);
        }
    };
}