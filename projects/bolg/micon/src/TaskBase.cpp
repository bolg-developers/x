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

    void TaskBase::create(uint8_t priority ,uint8_t coreID)
    {
        if(isTaskRun())
        {
            return;
        }

        xTaskCreatePinnedToCore(taskFunc, getTaskName(), 8192, m_taskFunc, (UBaseType_t)priority, &m_handle, (BaseType_t)coreID);

        m_isTaskRun = true;
    }

    void TaskBase::destroy()
    {
        if(!isTaskRun())
        {
            return;
        }

        m_isDestroyNotification = true;
    }

    void TaskBase::forcedDestroy()
    {
        if(!isTaskRun())
        {
            return;
        }

        m_isTaskRun = false;
        m_isDestroyNotification = false;

        vTaskDelete(m_handle);

        m_handle = nullptr;
    }

    bool TaskBase::isTaskRun()
    {
        return m_isTaskRun;
    }

    bool TaskBase::isDestroyNotification()
    {
        return m_isDestroyNotification;
    }
}