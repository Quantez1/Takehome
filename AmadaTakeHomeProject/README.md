Amada Android Take home test

NOTE

• Used this plug in to generate the POKOs from the returned JSON
  https://plugins.jetbrains.com/plugin/9960-json-to-kotlin-class-jsontokotlinclass-
• For the API response models, I would usually assume that everything is nullable unless it's an
  internal API and the developers for that are confident in its nullablity.

FUTURE LIBRARIES

• Timber for logging
• Hilt for dependency testing
• Compose NavGraph for navigation (for bonus functionality)

IMPROVEMENTS

• Create a separate version.gradle.kts to hold the artifact versions.
• Extract a separate Paginator class from the View Model
• For the Flickr API key, I would pass this in to the retrofit call from high above, either
  for the specific build type or extract it from a vault in the build process.
• In the retrofit code, would add an interceptor for debugging. Also would not use a try/catch
  block for determining if the API call failed.
• I didn't clean up the build.gradle.kts from the default project created by Android Studio.

BUGS

• size of thumbnail image is hard coded.
• pressing OK on keyboard doesn't kick off a search.