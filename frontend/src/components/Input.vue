<template>
  <div class="w-full">
    <label 
      v-if="label" 
      :for="id"
      class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-1.5 tracking-wide"
    >
      {{ label }}
      <span v-if="required" class="text-red-500 ml-0.5">*</span>
    </label>
    <div class="relative">
      <span 
        v-if="icon" 
        class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl pointer-events-none"
      >
        {{ icon }}
      </span>
      <input
        :id="id"
        :type="computedType"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :readonly="readonly"
        :maxlength="maxlength"
        :class="[
          'appearance-none block w-full py-3 border rounded-lg bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none transition-all sm:text-sm font-medium',
          icon ? 'pl-10' : 'pl-4',
          hasIconRight ? 'pr-12' : 'pr-4',
          error ? 'border-red-500 focus:ring-2 focus:ring-red-500 focus:border-transparent' : 'border-slate-200 dark:border-slate-700 focus:ring-2 focus:ring-primary focus:border-transparent',
          disabled ? 'opacity-50 cursor-not-allowed' : ''
        ]"
        @input="$emit('update:modelValue', $event.target.value)"
        @blur="$emit('blur', $event)"
        @focus="$emit('focus', $event)"
      />
      <button
        v-if="type === 'password'"
        type="button"
        class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 transition-colors"
        @click="showPassword = !showPassword"
      >
        <span class="material-symbols-outlined text-xl">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
      </button>
      <span
        v-else-if="iconRight"
        class="material-symbols-outlined absolute right-3 top-1/2 -translate-y-1/2 text-xl pointer-events-none"
        :class="error ? 'text-red-500' : 'text-slate-400'"
      >
        {{ error ? 'error' : iconRight }}
      </span>
    </div>
    <div v-if="error || hint" class="mt-1.5 flex items-start gap-1.5">
      <span v-if="error" class="material-symbols-outlined text-red-500 text-sm mt-0.5">error</span>
      <p :class="['text-xs font-medium', error ? 'text-red-500' : 'text-slate-500 dark:text-slate-400']">
        {{ error || hint }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  id: {
    type: String,
    default: () => `input-${Math.random().toString(36).substr(2, 9)}`
  },
  label: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  modelValue: {
    type: [String, Number],
    default: ''
  },
  placeholder: {
    type: String,
    default: ''
  },
  icon: {
    type: String,
    default: ''
  },
  iconRight: {
    type: String,
    default: ''
  },
  error: {
    type: String,
    default: ''
  },
  hint: {
    type: String,
    default: ''
  },
  required: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  readonly: {
    type: Boolean,
    default: false
  },
  maxlength: {
    type: [String, Number],
    default: null
  }
})

defineEmits(['update:modelValue', 'blur', 'focus'])

const showPassword = ref(false)

const computedType = computed(() => {
  if (props.type === 'password') {
    return showPassword.value ? 'text' : 'password'
  }
  return props.type
})

const hasIconRight = computed(() => {
  return props.type === 'password' || props.iconRight || props.error
})
</script>
