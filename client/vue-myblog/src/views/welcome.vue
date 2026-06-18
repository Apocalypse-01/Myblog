
<template>
    <div class="main">
  
        <div class="right">
            <router-view/>
        </div>

    </div>
</template>
    
<!--  -----------------------------------------------------  -->
    
<script setup lang="ts" name="Welcome">

</script lang="ts">
    
    
    
// ------------------------------------------------------------
    
<style scoped>

.main{
            background-color:rgba(127, 199, 215, 0);
            width: 100vw;
            height: 100vh;
            display: flex;
    
}


.right{
        
        width: 35%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        background-color:rgba(166, 155, 166, 0.171);
        border: 2px solid rgba(235, 105, 203, 0.349); 
        margin-left: 65%;
        
      

}

        
</style>
    