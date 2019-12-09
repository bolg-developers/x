#pragma once
#include <cstring>
#include <cstdint>
#include <IDefenser.h>
#include <memory>
#include <ICommandSender.h>
#include <string.h>

namespace bolg
{
    class Damage
    {
    private:

        std::shared_ptr<IDefenser> m_defenser;

        std::shared_ptr<ICommandSender> m_commandSender;

    public:

        Damage(std::shared_ptr<IDefenser>&& defenser, std::shared_ptr<ICommandSender> commandSender) :
         m_defenser(defenser),
         m_commandSender(commandSender) {}

        void applyDamage(int32_t damage)
        {
            std::vector<uint8_t> d;
            d.resize(4);

            ::memcpy(&d[0], &damage, 4);

            m_commandSender->addCommand(SendCommand::Hit, &d);
            m_defenser->applyDamage(damage);
        }
    };
}