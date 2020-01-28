// ----------------------------------------------------------------------------
//
// @file IRreceiveTask.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <TaskBase.h>
#include <FreeRTOS.h>
#include <freertos/task.h>
#include <IRremote.h>
#include "../Damage.h"
#include <memory>
#include <BolgConfig.h>
#include <vector>
#include <array>
#include <BolgLogger.h>

namespace bolg
{
    /// 赤外線受信タスク
    class IRreceiveTask : public TaskBase
    {
    private:

        std::vector<IRrecv*> irrecv;

        std::shared_ptr<Damage> m_damage;

        bool m_initialized = false;

    public:

        IRreceiveTask(std::shared_ptr<Damage>&& damage): m_damage(damage)
        {
            for(auto itr: BOLG_IR_RECEIVE_PINS)
            {
                irrecv.push_back(new IRrecv(itr));
            }
        }

        ~IRreceiveTask()
        {
            finalize();

            for(auto itr: irrecv)
            {
                delete itr;
            }
        }

        void task() override
        {
            while(!isDestroyNotification())
            {
                receive();
                delay(10);
            }
        }

        const char* getTaskName() override
        {
            return "IRreceive";
        }

        void init()
        {
            if(m_initialized)
            {
                return;
            }

            m_initialized = true;

            for(auto& itr : irrecv)
            {
                itr->enableIRIn();
            }
        }

        void finalize()
        {
            if(!m_initialized)
            {
                return;
            }

            m_initialized = false;

            for(const auto& itr : irrecv)
            {
                itr->resume();
                itr->disableIRIn();
            }
        }

        void destroy()
        {
            finalize();
            TaskBase::destroy();
        }

    private:

        void receive()
        {
            if(!m_initialized)
            {
                return;
            }

            decode_results result;

            int32_t id = 0;

            for(int32_t i = 0; i < irrecv.size(); i++)
            {
                if(irrecv[i]->decode(&result))
                {
                    if(result.decode_type == SONY)
                    {
                        id = static_cast<int32_t>(result.value);
                        BOLG_LOG("IRreceive  pin : %d   id : %d\n",BOLG_IR_RECEIVE_PINS[i],id);
                    }

                    irrecv[i]->resume();
                }
            }

            if(id)
            {
                m_damage->applyDamage(id);
            }
        }
    };
}