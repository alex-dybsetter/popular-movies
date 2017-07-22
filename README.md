# popular-movies

An Android app that allows users to view a grid of movie posters from popular movies.  The
movie database is pulled from The Movie Database API (https://www.themoviedb.org/).
A user can click on a movie poster and view a detail view of the movie's information, including
reviews and movie trailers that will launch in their web browser or the YouTube app. Users can also
save their favorite movies to a local SQLite database of their favorite movies.

# Installation

`git clone https://github.com/alexblass/popular-movies.git`
`cd popular-movies/`

**Your API key should be added in your `gradle.properties` file for correct functionality.**
You can obtain a key by registering an account at: https://www.themoviedb.org/

## 3rd Party Libs Credits

The Movie Database API: https://www.themoviedb.org/
This product uses the TMDb API but is not endorsed or certified by TMDb.

Butterknife by Jake Wharton: https://github.com/JakeWharton/butterknife
License: http://www.apache.org/licenses/LICENSE-2.0

Picasso by Square: http://square.github.io/picasso/
License: http://www.apache.org/licenses/LICENSE-2.0

## License

MIT License

Copyright (c) 2017 Alex Blass

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.