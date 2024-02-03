export default function useEvaluation(ref) {
    const evaluationQueue = ref([])

    const addEvaluationQueue = (item) => {
        const index = evaluationQueue.value.findIndex(o => o.id === item.id)

        if (index !== -1) {
            evaluationQueue.value[index] = item
        } else {
            evaluationQueue.value.push(item)
        }
    }

    return {
        evaluationQueue,
        addEvaluationQueue
    }
}