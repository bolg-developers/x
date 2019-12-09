// ----------------------------------------------------------------------------
//
// @file IGun.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>
#include <memory>

namespace bolg
{
    // 銃
    class IGun
    {
    public:

        //
        // @brief    発砲する
        //
        virtual bool shot() = 0;
    };
}