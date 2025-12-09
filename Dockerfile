FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy project files
COPY . .

# Build and install the project
RUN mvn -q -DskipTests clean install

# Use Maven image for final stage
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app

# Install system dependencies for Playwright and xvfb
RUN apt-get update && apt-get install -y \
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libxkbcommon0 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2 \
    libatspi2.0-0 \
    libxcursor1 \
    libgtk-3-0 \
    libpangocairo-1.0-0 \
    libcairo-gobject2 \
    libgdk-pixbuf-2.0-0 \
    # used for headless browser testing
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Copy built artifacts and source code
COPY --from=builder /app .

# Copy Maven local repository with built artifacts
COPY --from=builder /root/.m2/repository /root/.m2/repository

# Copy entrypoint script
COPY test-automation-ui/scripts/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]