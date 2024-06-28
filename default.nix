{ pkgs ? import <nixpkgs> {} }:

pkgs.stdenv.mkDerivation {
  name = "build-docs";
  src = ./.;
  buildInputs = [
    pkgs.pandoc
    pkgs.texlive.combined.scheme-medium
    pkgs.nodejs
    pkgs.gnumake
    pkgs.mermaid-cli
  ];
  shellHook = ''
    # Variables
    OUTPUT_DIR="docs"
    OUTPUT_FILE="documentation.pdf"
    DIAGRAMS_DIR="docs/diagrams"
    IMAGE_DIR="docs/tmp"

    # Create the images directory if it doesn't exist
    mkdir -p "$IMAGE_DIR"

    # Find Mermaid files and convert them to images
    for file in "$DIAGRAMS_DIR"/*.md; do
      basename=$(basename "$file" .md)
      mmdc -i "$file" -o "$IMAGE_DIR/$basename.png"
      # Since we know the pattern, directly rename each file
      mv "$IMAGE_DIR/$basename-1.png" "$IMAGE_DIR/$basename.png"
      echo "renamed file to: $IMAGE_DIR/$basename.png"
    done

    # Generate the PDF using Pandoc
    pandoc docs/index.md docs/motivation.md docs/architektur.md docs/komponenten.md docs/databaseDesign.md docs/useCases.md docs/verwendungszweck.md docs/zieluser.md --pdf-engine=pdflatex -o "$OUTPUT_DIR/$OUTPUT_FILE"

    # Check if the PDF was successfully created
    if [ $? -eq 0 ]; then
        echo "The PDF was successfully created: $OUTPUT_DIR/$OUTPUT_FILE"
        # Remove temporary images
        rm -rf "$IMAGE_DIR"
    else
        echo "Error creating the PDF"
        exit 1
    fi

    # Exit the shell
    exit
  '';
}