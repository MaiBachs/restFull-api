<template>
     <Breadcrumbs title="Layout Light"/>
      <div class="container-fluid">
            <div class="row starter-main">
                <projectdevelop/> 
                <simpleCard/> 
                <withHeader/>            
            </div>
      </div>
</template>
<script>
import projectdevelop from "./projectDevelop.vue"
import simpleCard from "./simpleCard.vue"
import withHeader from "./withHeader.vue"
export default {
     components:{
          projectdevelop,
          simpleCard,
          withHeader
     }
}
</script>