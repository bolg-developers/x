#include <TaskBase.h>
#include <FreeRTOS.h>
#include <freertos/task.h>
#include <functional>
#include <thread>
namespace bolg
{
    static void taskFunc(void* arg)
    {
        auto* func = static_cast<std::function<void()>*>(arg);

        (*func)();
    }

    TaskBase::TaskBase()
    {
        m_taskFunc = new std::function<void()>([this]
            {
                this->task();
                vTaskDelete(nullptr);
                this->destroy();
            }
        );
    }

    TaskBase::~TaskBase()
    {
        destroy();

        if(m_taskFunc)
        {
            delete m_taskFunc;
        }
    }

    void TaskBase::create()
    {
        if(isTaskRun())
        {
            return;
        }

        xTaskCreatePinnedToCore(taskFunc, getTaskName(), 8192, m_taskFunc, 1, &m_handle, 0);

        m_isTaskRun = true;
    }

    void TaskBase::destroy()
    {
        if(!isTaskRun())
        {
            return;
        }

        vTaskDelete(m_handle);

        m_isTaskRun = false;
    }

    bool TaskBase::isTaskRun()
    {
        return m_isTaskRun;
    }
}