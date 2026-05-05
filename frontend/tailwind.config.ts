import type { Config } from 'tailwindcss';

const config: Config = {
  content: ['./app/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        sand: '#f7f2e8',
        ink: '#1a1a1a',
        sage: '#0f766e',
        clay: '#c47f55',
        mist: '#f1ede4',
        midnight: '#1f2a2f',
      },
      fontFamily: {
        sans: ['var(--font-sans)', 'ui-sans-serif', 'system-ui'],
        serif: ['var(--font-serif)', 'ui-serif', 'Georgia'],
      },
      boxShadow: {
        soft: '0 20px 45px -35px rgba(31, 42, 47, 0.45)',
      },
      keyframes: {
        rise: {
          '0%': { opacity: '0', transform: 'translateY(16px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
      },
      animation: {
        rise: 'rise 0.6s ease-out both',
      },
    },
  },
  plugins: [],
};

export default config;
