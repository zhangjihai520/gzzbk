<template>
  <div>
    <mt-header fixed :title="title" style="background:#fff;color:#010">
      <span @click="back" slot="left">
        <mt-button icon="back">返回</mt-button>
      </span>
       <mt-button v-show="isShare" icon="more" slot="right"></mt-button>
    </mt-header>
  </div>
</template>
<script>
export default {
  name: "",
  data() {
    return {};
  },
  props: {
    title: {
      type: String,
    },
    isShare:{
      type:Boolean,
      default:false
    }
  },
  methods: {
    back() {
      this.$router.go(-1);
    },
  },
};
</script>
<style  scoped>
</style>