/*
 * file:        dumpBufDesc.cc
 * purpose:     helper to dump member offset - used to check matching definitions
 * created:     190901
 */
#define __STDC_FORMAT_MACROS
#include <inttypes.h>
#include "config.h"
#include "emc.hh"
#include "emc_nml.hh"


#define O(x) (((long) &(emcStatus->x)) - ((long)emcStatus))


struct MemberDef {
  const char* name;
  long offset;
  const char* cmt;
  };

static MemberDef StatusMembers[] = {
        {"taskMode",            O(task.mode)},
        {"taskState",           O(task.state)},
        {"execState",           O(task.execState)},
        {"interpState",         O(task.interpState)},
        {"call_level",          O(task.callLevel)},
        {"motionLine",          O(task.motionLine)},
        {"currentLine",         O(task.currentLine)},
        {"readLine",            O(task.readLine)},
        {"optional_stop",       O(task.optional_stop_state)},
        {"block_delete",        O(task.block_delete_state)},
        {"input_timeout",       O(task.input_timeout)},
        {"file",                O(task.file)},
        {"command",             O(task.command)},
        {"g5xOffsX",            O(task.g5x_offset.tran.x)},
        {"g5x_index",           O(task.g5x_index)},
        {"g92OffsX",            O(task.g92_offset.tran.x)},
        {"rotationXY",          O(task.rotation_xy)},
        {"toolOffsX",           O(task.toolOffset.tran.x)},
        {"activeGCodes",        O(task.activeGCodes)},
        {"activeMCodes",        O(task.activeMCodes)},
        {"activeSettings",      O(task.activeSettings)},
        {"programUnits",        O(task.programUnits)},
        {"interpreter_errcode", O(task.interpreter_errcode)},
        {"task_paused",         O(task.task_paused)},
        {"delay_left",          O(task.delayLeft)},
        {"queued_mdi_commands", O(task.queuedMDIcommands)}
};
/* */

/*
 *
 */
int main(int cArgs, char** pArgs) {
  for (const MemberDef *p = StatusMembers;
       p->offset > 0;
       ++p) {
      fprintf(stdout, "[%6ld] %s\n", p->offset, p->name);
      }
  }
