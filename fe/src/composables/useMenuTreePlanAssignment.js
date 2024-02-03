export default function usePlanAssignment(ref) {
    const expandsLevel = ref([])
    const expandsLevel2 = ref([])

    function changeInputLevel(id) {
        if (expandsLevel.value.includes(id)) {
          expandsLevel.value = expandsLevel.value.filter(o => o != id)
        } else {
          expandsLevel.value.push(id)
        }
      }
      function changeInputLevelMenu2(id) {
        if (expandsLevel2.value.includes(id)) {
            expandsLevel2.value = expandsLevel2.value.filter(o => o != id)
          } else {
            expandsLevel2.value.push(id)
          }
      }

    return {
        expandsLevel2,
        expandsLevel,
        changeInputLevel,
        changeInputLevelMenu2
    }
}