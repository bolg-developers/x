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
    // タスク基底クラス
    class TaskBase
    {
    private:

        std::function<void()>* m_taskFunc = nullptr;

        bool m_isTaskRun = false;

        TaskHandle_t m_handle;

    public:

        TaskBase();

        virtual ~TaskBase();

        //
        // @brief    タスクを起動する
        //
        void create();

        //
        // @brief    タスクを削除する
        //
        void destroy();

        //
        // @brief    タスクで実行する処理
        //
        virtual void task() = 0;

        //
        // @brief     タスク名を取得する
        //
        // @return    タスク名
        //
        virtual const char* getTaskName() = 0;

        //
        // @brief     タスクが実行されているか
        //
        // @return    true:
        //            false:
        //
        bool isTaskRun();
    };
}