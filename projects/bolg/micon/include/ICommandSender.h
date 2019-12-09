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
    enum class SendCommand
    {
        // レスポンス
        Response,

        // 被弾
        Hit,

        // 発砲
        Shot
    };

    // コマンド送信インターフェース
    class ICommandSender
    {
    public:

        virtual void addCommand(SendCommand command, std::vector<uint8_t>* args = nullptr) = 0;
    };
}