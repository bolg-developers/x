#pragma once
#include <cstdint>
#include <vector>

namespace bolg
{
    /// バイブレーションへのインターフェース
    class IVibrator
    {
    public:

        ///
        /// @brief     バイブレーションを振動させる
        ///
        /// @param     time   振動時間
        ///
        virtual void vibrate(int32_t time) = 0;

        ///
        /// @brief     バイブレーションを振動させる
        ///
        /// @param     pattern   振動パターン(ON,OFF,ON,OFF...)
        /// @param     loop      繰り返し回数
        ///
        virtual void vibrate(const std::vector<int32_t>& pattern, int32_t loop = 0) = 0;

        ///
        /// @brief     バイブレーションを停止する
        ///
        virtual void cancel() = 0;
    };

    ///
    /// @brief     バイブレーターへのインターフェースを取得する
    ///
    /// @return    バイブレーターへのインターフェース
    ///
    IVibrator* GetVibrator();
}