// ----------------------------------------------------------------------------
//
// @file CommandReceiveTask.h
//
// @author yudai yamaguchi
//
// ----------------------------------------------------------------------------
#pragma once
#include <ICommandReceiver.h>
#include <TaskBase.h>
#include <unordered_map>
#include <Stream.h>
#include <memory>
#include <BluetoothSerial.h>
#include <BolgConfig.h>
#include <esp_log.h>

namespace bolg
{
    class CommandReveiveTask : public TaskBase, public ICommandReceiver
    {
    public:

        struct CommandData
        {
            CommandReceiveCallBack callBack;
            int32_t argByte;
        };

    private:

        std::unordered_map<uint8_t, CommandData> m_commandMap;

        std::shared_ptr<Stream> m_stream;

        uint8_t* m_buffer;

        const int32_t BUFFER_SIZE = 256;

    public:

        CommandReveiveTask() = delete;

        CommandReveiveTask(std::shared_ptr<Stream> stream):m_stream(stream)
        {
            m_buffer = new uint8_t[BUFFER_SIZE];
        }

        ~CommandReveiveTask()
        {
            delete[] m_buffer;
        }

        void setCommandListener(uint8_t command, CommandReceiveCallBack callback, int32_t argByte) override
        {
            CommandData commandData{callback, argByte};

            m_commandMap.emplace(command, commandData);
        }

        void deleteCommandListener(uint8_t command) override
        {
            auto itr = m_commandMap.find(command);

            if(itr == m_commandMap.end())
            {
                return;
            }

            m_commandMap.erase(itr);
        }

        void task() override
        {
            while(true)
            {
                // スタートバイトが来るまで待機
                while(static_cast<uint8_t>(m_stream->read()) != COMMAND_START_BYTE)
                {
                    delay(1);
                }

                while(!m_stream->available())
                {
                    delay(1);
                }

                uint8_t command = m_stream->read();

                auto commandData = m_commandMap.find(command);

                if(commandData != m_commandMap.end())
                {
                    while(m_stream->available() < commandData->second.argByte)
                    {
                        delay(1);
                    }

                    m_stream->readBytes(m_buffer,commandData->second.argByte);

                    std::vector<uint8_t> arg;

                    arg.resize(commandData->second.argByte);

                    ::memcpy(&arg[0],m_buffer,commandData->second.argByte);

                    commandData->second.callBack(arg);
                }

                while(static_cast<uint8_t>(m_stream->read()) != COMMAND_END_BYTE)
                {
                    delay(1);
                }
            }
        }

        const char* getTaskName() override
        {
            return "CommandReceiver";
        }
    };
}
