// ----------------------------------------------------------------------------
//
// @file IDefenser.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

namespace bolg
{
    // 防衛インターフェース
    class IDefenser
    {
    public:

        //
        // @brief     ダメージを受ける
        //
        virtual void applyDamage(int32_t damage) = 0;
    };
}