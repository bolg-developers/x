#pragma once
#include <IGun.h>
#include <IPlayer.h>
#include <IShotManager.h>
#include <memory>

namespace bolg
{
    class Gun : public IGun
    {
    private:

        std::shared_ptr<IPlayer> m_owner;

        std::shared_ptr<IShotManager> m_shotManager;

        std::shared_ptr<IShot> m_shot;

    public:

        Gun(std::shared_ptr<IPlayer> owner, std::shared_ptr<IShotManager> shotManager, std::shared_ptr<IShot> shot):
            m_owner(owner),
            m_shotManager(shotManager),
            m_shot(shot){}

        void setOwner(std::shared_ptr<IPlayer> owner)
        {
            m_owner = owner;
        }

        bool shot()
        {
            m_shotManager->createShot(m_shot);
            return true;
        }
    };
}