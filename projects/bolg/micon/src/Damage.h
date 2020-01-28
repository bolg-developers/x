#pragma once
#include <cstring>
#include <cstdint>
#include <IDefenser.h>
#include <memory>
#include <ICommandSender.h>
#include <string.h>
#include <IVibrator.h>
#include <IPlayer.h>
#include <BolgLogger.h>

namespace bolg
{
    /// ダメージ
    class Damage
    {
    private:

        std::shared_ptr<IDefenser> m_defenser;

        std::shared_ptr<IPlayer> m_player;

        std::shared_ptr<ICommandSender> m_commandSender;

    public:

        Damage(std::shared_ptr<IDefenser> defenser,std::shared_ptr<IPlayer> player, std::shared_ptr<ICommandSender> commandSender) :
            m_defenser(defenser),
            m_player(player),
            m_commandSender(commandSender) {}

        ///
        /// @brief    ダメージを受ける
        ///
        /// @param    id    攻撃者ID
        ///
        void applyDamage(int32_t id)
        {
            if(m_player->getUserID() == id)
            {
                return;
            }

            std::vector<uint8_t> d;

            d.resize(4);

            ::memcpy(&d[0], &id, 4);

            const std::vector<int32_t> pattern = {1000,100,200};

            GetVibrator()->vibrate(pattern);

            m_commandSender->addCommand(SendCommand::Hit, &d);

            m_defenser->applyDamage(id);
        }
    };
}