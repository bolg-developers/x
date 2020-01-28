// ----------------------------------------------------------------------------
//
// @file TaskBase.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <cstdint>
#include <functional>
#include <FreeRTOS.h>
#include <freertos/task.h>

namespace bolg
{
    /// タスク基底クラス
    class TaskBase
    {
    private:

        std::function<void()>* m_taskFunc = nullptr;

        bool m_isTaskRun = false;

        bool m_isDestroyNotification = false;

        TaskHandle_t m_handle;

    public:

        TaskBase();

        virtual ~TaskBase();

        ///
        /// @brief    タスクを起動する
        ///
        /// @param    priority   優先度
        /// @param    coreID     実行CPUcoreID
        ///
        void create(uint8_t priority, uint8_t coreID);

        ///
        /// @brief    タスクを削除する
        ///
        void destroy();

        ///
        /// @brief    強制タスクを削除する
        ///
        void forcedDestroy();

        ///
        /// @brief    タスクで実行する処理
        /// @details  isDestroyNotificationがtrueを返す時は即座にこの関数をリターンしなければなりません
        ///
        virtual void task() = 0;

        ///
        /// @brief     タスク名を取得する
        ///
        /// @return    タスク名
        ///
        virtual const char* getTaskName() = 0;

        ///
        /// @brief     タスクが実行されているか
        ///
        /// @retval    true    実行中
        /// @retval    false   未実行
        ///
        bool isTaskRun();

    protected:

        ///
        /// @brief    タスク削除要請の取得
        ///
        /// @retval   true     削除要請あり
        /// @retval   false    継続実行可能
        ///
        bool isDestroyNotification();
    };
}