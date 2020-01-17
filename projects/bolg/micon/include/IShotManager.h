// ----------------------------------------------------------------------------
//
// @file IShotManager.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

namespace bolg
{
    /// 発砲データ
    struct ShotData
    {
        //! 赤外線データ
        uint32_t data;

        //! 送信ビット数
        int32_t bits;
    };

    /// 発砲インターフェース
    class IShot
    {
    public:

        ///
        /// @brief    発砲データを取得する
        ///
        /// @return   発砲データ
        ///
        virtual ShotData getShotData() = 0;
    };

    /// 発砲管理
    class IShotManager
    {
    public:

        ///
        /// @brief    ショットを作成する
        ///
        /// @param    shot : 生成するショット
        ///
        virtual void createShot(std::shared_ptr<IShot>& shot) = 0;
    };
}