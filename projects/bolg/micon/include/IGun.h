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
    /// 銃のインターフェース
    class IGun
    {
    public:

        ///
        /// @brief   発砲する
        ///
        /// @retval  true  : 成功
        /// @retval  false : エラー
        ///
        virtual bool shot() = 0;
    };
}