# Edge Detection Web Viewer

TypeScript-based web viewer for displaying processed camera frames from the Android app.

## Features

- 📊 **Real-time Stats Display** - FPS, resolution, processing mode
- 🖼️ **Frame Display** - Canvas-based rendering
- 📁 **File Upload** - Load and display static images
- 🎨 **Modern UI** - Clean, responsive design

## Setup

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

```bash
cd web
npm install
```

### Build

```bash
npm run build
```

This compiles TypeScript files from `src/` to `dist/` as JavaScript.

### Run

```bash
npm run serve
```

Opens `http://localhost:8080` in your browser.

## Architecture

### Directory Structure

```
web/
├── src/
│   ├── types.ts          # TypeScript interfaces
│   ├── FrameViewer.ts    # Main viewer class
│   └── main.ts           # Entry point
├── dist/
│   ├── index.html        # Web page
│   ├── styles.css        # Styling
│   └── *.js              # Compiled TypeScript
├── package.json
└── tsconfig.json
```

### Key Components

#### FrameViewer Class
- Manages canvas rendering
- Updates frame statistics
- Calculates FPS
- Handles frame data (base64 images)

#### Types
- `FrameData` - Frame information
- `FrameStats` - Statistics
- `ProcessingMode` - Processing modes enum

## Usage

### Loading Frames

The viewer can display frames in two ways:

1. **Sample Frame** - Generated demo frame
2. **Upload Image** - Load image from file system

### Future Integration

This viewer is designed to receive frames from the Android app via:
- WebSocket connection
- HTTP endpoint
- File export/import

## Technologies

- **TypeScript** - Type-safe JavaScript
- **HTML5 Canvas** - Frame rendering
- **CSS3** - Modern styling
- **ES Modules** - Modular code structure

## Development

### Watch Mode

```bash
npm run watch
```

Auto-compiles TypeScript on file changes.

### Adding Features

To extend the viewer:

1. Add types in `src/types.ts`
2. Implement logic in `src/FrameViewer.ts`
3. Update UI in `dist/index.html`
4. Rebuild with `npm run build`

