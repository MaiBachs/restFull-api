export default function useAssign(ref) {
    const assignQueue = ref([])
    const editValueAssignQueue = ref([])
    const addAssignQueue = (item) => {
        const index = assignQueue.value.findIndex(o => o.plan_job_id === item.plan_job_id)
        if (index !== -1) {
            assignQueue.value[index] = item
        } else {
            assignQueue.value.push(item)
        }
    }
    const editAssignQueue = (item) => {
        const index = assignQueue.value.findIndex(o => o.job_id === item.job_id)
        if (index !== -1) {
            assignQueue.value[index] = item
        } else {
            assignQueue.value.push(item)
        }
    }
    const valueEditAssignQueue = (item) => {
        console.log(item)
        console.log(editValueAssignQueue.value)
        const index = assignQueue.value.findIndex(o => o.job_id === item.job_id)
        if (index !== -1) {
            editValueAssignQueue.value[index] = item
        } else {
            editValueAssignQueue.value.push(item)
        }
    }
    const deleteAssignQueue = (item) => {
        const index = assignQueue.value.findIndex(o => o.plan_job_id === item.plan_job_id)
        if (index !== -1) {
            assignQueue.value[index] = item
        } else {
            assignQueue.value.push(item)
        }
    }

    const deleteAssignQueueLevel2 = (item) => {
        const index = assignQueue.value.findIndex(o => o.plan_job_parent_id2 === item.plan_job_parent_id2)
        if (index !== -1) {
            assignQueue.value[index] = item
        } else {
            assignQueue.value.push(item)
        }
    }

    const deleteAssignQueueLevel1 = (item) => {
        const index = assignQueue.value.findIndex(o => o.plan_job_parent_id1 === item.plan_job_parent_id1)
        if (index !== -1) {
            assignQueue.value[index] = item
        } else {
            assignQueue.value.push(item)
        }
    }

    return {
        assignQueue,
        editValueAssignQueue,
        valueEditAssignQueue,
        addAssignQueue,
        editAssignQueue,
        deleteAssignQueue,
        deleteAssignQueueLevel2,
        deleteAssignQueueLevel1
    }
}