import type { Metadata } from 'next';
import type { ReactNode } from 'react';
import Link from 'next/link';
import { Fraunces, Space_Grotesk } from 'next/font/google';

import './globals.css';

const spaceGrotesk = Space_Grotesk({
  subsets: ['latin'],
  variable: '--font-sans',
  display: 'swap',
});

const fraunces = Fraunces({
  subsets: ['latin'],
  variable: '--font-serif',
  display: 'swap',
});

export const metadata: Metadata = {
  title: 'Students Gateway',
  description: 'Microservices dashboard for students, departments, and notes.',
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en" className={`${spaceGrotesk.variable} ${fraunces.variable}`}>
      <body className="min-h-screen bg-sand text-ink">
        <div className="page-backdrop" aria-hidden="true" />
        <div className="relative mx-auto flex min-h-screen max-w-6xl flex-col px-6 pb-12 pt-8">
          <header className="flex flex-col gap-4 rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur md:flex-row md:items-center md:justify-between">
            <div className="space-y-2">
              <p className="text-sm uppercase tracking-[0.3em] text-sage">Students Platform</p>
              <h1 className="font-serif text-3xl md:text-4xl">Gateway-ready admin</h1>
              <p className="max-w-xl text-sm text-midnight/70">
                Manage students and departments through the API Gateway with microservice routing.
              </p>
            </div>
            <nav className="flex flex-wrap gap-3">
              <Link
                href="/etudiants"
                className="rounded-full border border-sage/50 px-5 py-2 text-sm font-semibold text-sage transition hover:bg-sage hover:text-white"
              >
                Etudiants
              </Link>
              <Link
                href="/departements"
                className="rounded-full border border-clay/50 px-5 py-2 text-sm font-semibold text-clay transition hover:bg-clay hover:text-white"
              >
                Departements
              </Link>
            </nav>
          </header>
          <main className="mt-10 flex-1 animate-rise">{children}</main>
          <footer className="mt-16 text-xs uppercase tracking-[0.2em] text-midnight/60">
            API Gateway entrypoint: http://localhost:8080
          </footer>
        </div>
      </body>
    </html>
  );
}
