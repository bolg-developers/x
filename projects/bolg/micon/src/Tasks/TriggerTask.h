// ----------------------------------------------------------------------------
//
// @file TriggerTask.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <TaskBase.h>
#include <IAttacker.h>
#include <memory>
#include <Arduino.h>
#include <BolgConfig.h>

namespace bolg
{
    class TriggerTask :public TaskBase
    {
    private:

        std::shared_ptr<IAttacker> m_attacker;

        uint8_t m_triggerPin = BOLG_TRIGGER_PIN;

    public:

        TriggerTask(std::shared_ptr<IAttacker> attacker):m_attacker(attacker){}

        void task()
        {
            pinMode(m_triggerPin,INPUT);

            int32_t prev = digitalRead(m_triggerPin);

            while(true)
            {
                int32_t state = digitalRead(m_triggerPin);

                if(state == HIGH && prev == LOW)
                {
                    m_attacker->attack();
                }

                prev = state;

                delay(10);
            }
        }

        const char* getTaskName()
        {
            return "TriggerTask";
        }
    };
}