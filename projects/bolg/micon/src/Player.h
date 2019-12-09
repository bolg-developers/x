#pragma once
#include <IPlayer.h>
#include <IAttacker.h>
#include <IDefenser.h>
#include <Arduino.h>
#include <IGun.h>
#include "Gun.h"

namespace bolg
{
    // プレイヤー
    class Player : public IPlayer, public IAttacker, public IDefenser
    {
    private:

        int32_t m_userID = 0;

        std::shared_ptr<IGun> m_gun;

    public:

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
            m_gun->shot();
        }

        void applyDamage(int32_t damage) override
        {
            //Serial.printf("damage = %d\n", damage);
        }
    };
}