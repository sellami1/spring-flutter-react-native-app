import Link from 'next/link';

export default function HomePage() {
  return (
    <section className="grid gap-8 md:grid-cols-[1.2fr_0.8fr]">
      <div className="rounded-3xl border border-white/60 bg-white/70 p-8 shadow-soft backdrop-blur">
        <p className="text-sm uppercase tracking-[0.3em] text-sage">Sprint 3</p>
        <h2 className="mt-3 font-serif text-4xl">Microservices, one front door.</h2>
        <p className="mt-4 text-sm text-midnight/70">
          Browse students, manage departments, and validate data through Eureka discovery and the
          API Gateway.
        </p>
        <div className="mt-6 flex flex-wrap gap-3">
          <Link
            href="/etudiants"
            className="rounded-full bg-sage px-6 py-3 text-sm font-semibold text-white"
          >
            Open Students
          </Link>
          <Link
            href="/departements"
            className="rounded-full border border-clay/50 px-6 py-3 text-sm font-semibold text-clay"
          >
            Manage Departments
          </Link>
        </div>
      </div>
      <div className="space-y-5">
        <div className="rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur">
          <h3 className="text-lg font-semibold">Routing coverage</h3>
          <p className="mt-3 text-sm text-midnight/70">
            All requests flow through the gateway on port 8080 and are routed to the right service
            using Eureka.
          </p>
        </div>
        <div className="rounded-3xl border border-white/60 bg-white/70 p-6 shadow-soft backdrop-blur">
          <h3 className="text-lg font-semibold">Live CRUD</h3>
          <p className="mt-3 text-sm text-midnight/70">
            Create, update, and edit resources without leaving the browser. Each form triggers a
            refresh to show the new state.
          </p>
        </div>
      </div>
    </section>
  );
}
