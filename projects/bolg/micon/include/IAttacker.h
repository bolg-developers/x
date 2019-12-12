// ----------------------------------------------------------------------------
//
// @file IAttacker.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

namespace bolg
{
    // 攻撃者インターフェース
    class IAttacker
    {
    public:

        //
        // @bridef    攻撃をする
        //
        virtual void attack() = 0;
    };
}