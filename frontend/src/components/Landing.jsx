
const Landing = () => {
  return (
    <section id="about" className="w-full max-w-4xl mx-auto px-4 py-8 pt-24">
      {/* Hero */}
      <div className="text-center mb-12">
        <h1 className="text-5xl font-bold text-white mb-4">
          Turn your thoughts into <span className="text-yellow-400">music</span>
        </h1>
        <p className="text-xl text-gray-200 max-w-2xl mx-auto">
          Write down your thoughts, automatically uncover your mood with AI, and get a
          perfectly curated song recommendation to match how you feel.
        </p>
      </div>

      {/* Steps Grid */}
      <div className="grid md:grid-cols-3 gap-8 mb-12">
        {/* Step 1 */}
        <div className="bg-white/10 backdrop-blur-md rounded-xl p-8 border border-white/20 text-center">
          <div className="w-16 h-16 bg-mint-400/20 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">✍️</span>
          </div>
          <h2 className="text-2xl font-bold text-white mb-2">Log Your Journal</h2>
          <p className="text-gray-200">
            Quickly jot down entries anywhere—desktop or mobile. No format, no fuss.
          </p>
        </div>

        {/* Step 2 */}
        <div className="bg-white/10 backdrop-blur-md rounded-xl p-8 border border-white/20 text-center">
          <div className="w-16 h-16 bg-mint-400/20 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">🤖</span>
          </div>
          <h2 className="text-2xl font-bold text-white mb-2">AI Mood Analysis</h2>
          <p className="text-gray-200">
            Our AI reads your words and tells you exactly how you’re feeling under the surface.
          </p>
        </div>

        {/* Step 3 */}
        <div className="bg-white/10 backdrop-blur-md rounded-xl p-8 border border-white/20 text-center">
          <div className="w-16 h-16 bg-mint-400/20 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">🎵</span>
          </div>
          <h2 className="text-2xl font-bold text-white mb-2">Song Recommendation</h2>
          <p className="text-gray-200">
            Based on your mood, we handpick a track from Spotify so you can vibe exactly how you feel.
          </p>
        </div>
      </div>

      {/* Footer CTA */}
      <div className="text-center mt-12">
        <p className="text-gray-300 text-lg">
          Ready to turn your feelings into melodies? 
          <span className="text-yellow-400 font-semibold"> Start journaling now!</span>
        </p>
      </div>
    </section>
  );
};

export default Landing;
