// ----------------------------------------------------------------------------
//
// @file ICommandReceiver.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <functional>
#include <vector>

namespace bolg
{
    enum class ReceiveCommand
    {
        // ユーザーIDを設定する
        SET_USER_ID,

        // ゲーム状態を設定する
        SET_GAME_STATE,

        // レスポンス
        RESPONSE
    };

    using CommandReceiveCallBack = std::function<void(std::vector<uint8_t>&)>;

    // コマンド受信のインターフェース
    class ICommandReceiver
    {
    public:

        //
        // @brief     コマンドコールバックを登録する
        //
        // @param      command   コマンド
        // @param      callback  コマンドを受信したときに呼ばれるコールバック
        // @param      argByte   コマンド引数のバイト数
        //
        virtual void setCommandListener(uint8_t command, CommandReceiveCallBack callback, int32_t argByte) = 0;

        //
        // @brief     コマンドコールバックを削除する
        //
        // @param      command   削除するコマンドコールバックのコマンド
        //
        virtual void deleteCommandListener(uint8_t command);
    };
}