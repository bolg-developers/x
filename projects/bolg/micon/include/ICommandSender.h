// ----------------------------------------------------------------------------
//
// @file ICommandSender.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <vector>

namespace bolg
{
    ///
    /// @enum  SendCommand
    ///
    /// @brief 送信コマンド
    ///
    enum class SendCommand
    {
        //!< レスポンス
        Response,

        //! 被弾
        Hit,

        //! 発砲
        Shot
    };

    /// コマンド送信インターフェース
    class ICommandSender
    {
    public:

        ///
        /// @brief     コマンドを送信する
        ///
        /// @param      command   コマンド
        /// @param      args      コマンド引数
        ///
        virtual void addCommand(SendCommand command, std::vector<uint8_t>* args = nullptr) = 0;
    };
}