#pragma once
#include <IPlayer.h>
#include <IAttacker.h>
#include <IDefenser.h>
#include <Arduino.h>
#include <IGun.h>
#include "Gun.h"
#include <BolgLogger.h>

namespace bolg
{
    /// プレイヤー
    class Player : public IPlayer, public IAttacker, public IDefenser
    {
    private:

        int32_t m_userID = 0;

        std::shared_ptr<IGun> m_gun;

    public:

        ///
        /// @brief   初期化
        ///
        /// @param   userID  ユーザーID
        /// @param   gun     使用する銃
        ///
        void init(int32_t userID, std::shared_ptr<IGun> gun)
        {
            m_userID = userID;
            m_gun = gun;
        }

        int32_t getUserID() override
        {
            return m_userID;
        }

        void attack() override
        {
            BOLG_LOG("Attack\n");
            m_gun->shot();
        }

        void applyDamage(int32_t damage) override
        {
            BOLG_LOG("damage id : %d\n", damage);
        }
    };
}