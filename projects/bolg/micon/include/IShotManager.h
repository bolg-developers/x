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
    struct ShotData
    {
        // 赤外線データ
        uint32_t data;

        // 送信ビット数
        int32_t bits;
    };

    class IShot
    {
    public:

        virtual ShotData getShotData() = 0;
    };

    class IShotManager
    {
    public:

        //
        // @brief    ショットを作成する
        //
        // @param    shot : 生成するショット
        //
        virtual void createShot(std::shared_ptr<IShot> shot) = 0;
    };
}