#pragma once
#include <TaskBase.h>
#include <IVibrator.h>
#include <Arduino.h>
#include <BolgConfig.h>
#include <BolgLogger.h>

namespace bolg
{
    /// バイブレータータスク
    class VibratorTask : public TaskBase , public IVibrator
    {
    private:

        std::vector<int32_t> m_pattern;

        int32_t m_loop = 0;

        bool m_changed = false;

    public:

        VibratorTask()
        {
            create(0,0);
        }

        void task()override
        {
            while(!isDestroyNotification())
            {
                while(!m_changed)
                {
                    delay(1);
                }

                m_changed = false;

                BOLG_LOG("Vibrator Act\n");

                for(int32_t i = 0; i < m_loop + 1; i++)
                {
                    if(!vibrate())
                    {
                        break;
                    }
                }

                if(!m_changed)
                {
                    m_loop = 0;
                    m_pattern.clear();
                }
            }
        }

        const char* getTaskName()override
        {
            return "vibratorTask";
        }

        void vibrate(int32_t time)override
        {
            m_pattern.clear();
            m_pattern.push_back(time);
            m_pattern.push_back(0);
            m_loop    = 0;
            m_changed = true;
        }

        void vibrate(const std::vector<int32_t>& pattern, int32_t loop = 0)override
        {
            m_pattern = pattern;
            m_loop = loop;
            m_changed = true;
        }

        void cancel()override
        {
            m_pattern.clear();
            m_loop    = 0;
            m_changed = true;

            BOLG_LOG("Vibrator Cancel\n");
        }

    private:

        bool vibrateWait(int32_t time)
        {
            for(int32_t i = 0; i < time; i++)
            {
                if(m_changed)
                {
                    return false;
                }
                delay(1);
            }

            return true;
        }

        bool vibrate()
        {
            for(int32_t i = 0; i<m_pattern.size(); i++)
            {
                digitalWrite(BOLG_VIBRATION_PIN, i % 2 ? LOW : HIGH);

                if(!vibrateWait(m_pattern[i]))
                {
                    return false;
                }
            }

            digitalWrite(BOLG_VIBRATION_PIN, LOW);

            return true;
        }
    };
}