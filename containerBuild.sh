#!/usr/bin/env sh

# Initialisiere Variablen
app_name="immobilienverwaltung"
username="dav354"
password=""
image_version="latest"

# Hilfefunktion für die Benutzung des Skripts
usage() {
  echo "Usage: $0 --password PASSWORD [--username USERNAME] [--app_name NAME] [--version VERSION]"
  echo "  --app_name  NAME       Name of the application (default: '$app_name')"
  echo "  --username  USERNAME   GitHub username (default: '$username')"
  echo "  --password  PASSWORD   GitHub token or password"
  echo "  --version   VERSION    Docker image version (default: '$image_version')"
  exit 1
}

# Argumente parsen
while [ "$#" -gt 0 ]; do
  case "$1" in
    -n|--app_name)
      app_name="$2"
      shift 2
      ;;
    -u|--username)
      username="$2"
      shift 2
      ;;
    -p|--password)
      password="$2"
      shift 2
      ;;
    --version)
      image_version="$2"
      shift 2
      ;;
    -h|--help)
      usage
      ;;
    *)
      echo "Unknown option: $1"
      usage
      ;;
  esac
done

# Überprüfe, ob alle erforderlichen Variablen gesetzt sind
if [ -z "$app_name" ] || [ -z "$username" ] || [ -z "$password" ] || [ -z "$image_version" ] ; then
  echo "Error: All parameters (--app_name, --username, --password, --image_version) are required."
  usage
fi

# Maven-Projekt reinigen und bauen
if ! mvn clean package -Pproduction; then
  echo "Maven build failed"
  exit 1
fi

# Docker-Image bauen
if ! docker build -t "$username/$app_name:$image_version" .; then
  echo "Docker build failed"
  exit 1
fi

# Docker-Login sicherer gestalten
# Vermeide die Verwendung von echo für das Passwort
echo "Logging into Docker..."
if ! echo "$password" | docker login ghcr.io -u "$username" --password-stdin; then
  echo "Docker login failed"
  exit 1
fi

# Docker-Image taggen
if ! docker tag "$username/$app_name:$image_version" ghcr.io/$username/$app_name:$image_version; then
  echo "Failed to tag Docker image"
  exit 1
fi

# Docker-Image pushen
echo "Pushing Docker image..."
if ! docker push ghcr.io/$username/$app_name:$image_version; then
  echo "Failed to push Docker image"
  exit 1
fi

echo "Docker image pushed successfully"