// ----------------------------------------------------------------------------
//
// @file IRsendTask.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <TaskBase.h>
#include <FreeRTOS.h>
#include <freertos/task.h>

namespace bolg
{
    class IRsendTask : public TaskBase
    {
    public:

        void task() override
        {
            while(true)
            {
                vTaskDelay(1);
            }
        }

        const char* getTaskName() override
        {
            return "IRsend";
        }
    };
}