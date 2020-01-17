// ----------------------------------------------------------------------------
//
// @file CommandSendTask.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <TaskBase.h>
#include <vector>
#include <cstring>
#include <memory>
#include <BluetoothSerial.h>
#include <ICommandSender.h>
#include <BolgLogger.h>

namespace bolg
{
    /// コマンド送信タスク
    class CommandSendTask : public TaskBase, public ICommandSender
    {
    private:

        std::shared_ptr<Stream> m_stream;

        uint8_t* m_packetBuffer = nullptr;

        uint8_t* m_packetSizeBuffer = nullptr;

        const int32_t BUFFER_SIZE = UINT8_MAX;

        int32_t m_packetPos = 0;

        int32_t m_packetSizePos = 0;

        SemaphoreHandle_t m_xMutex = nullptr;

    public:

        CommandSendTask(std::shared_ptr<Stream> stream):m_stream(stream)
        {
            m_xMutex = xSemaphoreCreateRecursiveMutex();

            if(!m_xMutex)
            {
                exit(1);
            }

            m_packetBuffer     = new uint8_t[BUFFER_SIZE];
            m_packetSizeBuffer = new uint8_t[BUFFER_SIZE];
        }

        ~CommandSendTask()
        {
            delete[] m_packetBuffer;
            delete[] m_packetSizeBuffer;
        }

        void task() override
        {
            while(!isDestroyNotification())
            {
                flush();
                delay(1);
            }
        }

        const char* getTaskName() override
        {
            return "BluetoothSendTask";
        }

        void addCommand(SendCommand command, std::vector<uint8_t>* args = nullptr) override
        {
            if (!xSemaphoreTakeRecursive(m_xMutex, portMAX_DELAY))
            {
                return;
            }

            m_packetBuffer[m_packetSizePos] = static_cast<uint8_t>(command);
            m_packetSizePos++;
            m_packetSizeBuffer[m_packetPos] = sizeof(uint8_t);

            BOLG_LOG("CommandSend  command : %d  arg : ", command);

            // 引数あり
            if(args)
            {
                ::memcpy(m_packetBuffer + m_packetSizePos,&(*args)[0],args->size());
                m_packetSizePos                 += args->size();
                m_packetSizeBuffer[m_packetPos] += args->size();

                for(const auto itr : *args)
                {
                    BOLG_LOG("%d", itr);
                }
            }

            BOLG_LOG("\n");

            m_packetPos++;
            xSemaphoreGiveRecursive(m_xMutex);
        }

    private:

        void flush()
        {
            if(!m_packetPos)
            {
                return;
            }

            if (!xSemaphoreTakeRecursive(m_xMutex, portMAX_DELAY))
            {
                return;
            }

            int32_t  packetPos = 0;
            uint8_t* pPacket   = m_packetBuffer;

            while(m_packetPos != packetPos)
            {
                // packet送信
                m_stream->write(COMMAND_START_BYTE);
                m_stream->write(pPacket, m_packetSizeBuffer[packetPos]);
                m_stream->write(COMMAND_END_BYTE);
                pPacket += m_packetSizeBuffer[packetPos];
                packetPos++;
            }

            m_packetPos     = 0;
            m_packetSizePos = 0;

            xSemaphoreGiveRecursive(m_xMutex);
        }
    };
}