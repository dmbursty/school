#ifndef _MESSAGES_H
#define _MESSAGES_H

#include "rtx_inc.h"

VOID k_message_init(VOID);
SINT32 k_delayed_send (UINT32 process_id, UINT32 msg_type, VOID* Message, UINT32 delay);
VOID* k_receive_message (UINT32* sender_ID, UINT32* msg_type);

#endif
