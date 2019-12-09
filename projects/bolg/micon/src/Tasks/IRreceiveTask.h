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

namespace bolg
{
    // 赤外線受信タスク
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
            init();

            while(true)
            {
                receive();
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

            for(auto& itr : irrecv)
            {
                itr->enableIRIn();
            }

            m_initialized = true;
        }

        void finalize()
        {
            if(!m_initialized)
            {
                return;
            }

            m_initialized = false;

            for(auto& itr : irrecv)
            {
            
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
            decode_results result;

            for(auto& itr : irrecv)
            {
                if(itr->decode(&result))
                {
                    if(result.decode_type == SONY)
                    {
                        m_damage->applyDamage(result.value);
                    }
                    itr->resume();
                }
            }
            delay(1);
        }
    };
}