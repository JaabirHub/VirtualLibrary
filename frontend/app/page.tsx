import Link from "next/link";

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="text-center max-w-xl px-6">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Virtual Library
        </h1>
        <p className="text-gray-500 text-lg mb-8">
          Search, track, and manage your reading list in one place.
        </p>
        <div className="flex gap-4 justify-center">
          <Link
            href="/register"
            className="bg-gray-900 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition-colors"
          >
            Get Started
          </Link>
          <Link
            href="/login"
            className="border border-gray-300 text-gray-700 px-6 py-3 rounded-lg hover:bg-gray-100 transition-colors"
          >
            Login
          </Link>
        </div>
      </div>
    </div>
  );
}