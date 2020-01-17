#pragma once
#include <IGun.h>
#include <IPlayer.h>
#include <IShotManager.h>
#include <memory>
#include <IVibrator.h>

namespace bolg
{
    /// 銃
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

        ///
        /// @brief   銃の所有者を設定する
        ///
        /// @param   owner   所有者
        ///
        void setOwner(std::shared_ptr<IPlayer> owner)
        {
            m_owner = owner;
        }

        bool shot() override
        {
            m_shotManager->createShot(m_shot);
            return true;
        }
    };
}