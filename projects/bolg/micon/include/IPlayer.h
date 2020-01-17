// ----------------------------------------------------------------------------
//
// @file IPlayer.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>

namespace bolg
{
    /// プレイヤーインターフェース
    class IPlayer
    {
    public:

        ///
        /// @brief    プレイヤーIDを取得する
        ///
        /// @return   自身のユーザーID
        ///
        virtual int32_t getUserID() = 0;
    };
}