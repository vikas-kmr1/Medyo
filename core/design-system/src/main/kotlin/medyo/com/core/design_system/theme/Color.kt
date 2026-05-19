package medyo.com.core.design_system.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Light Theme Tokens (Classic Clean White)
// ==========================================
val LightBackground = Color(0xFFF8FAFC)       // Crisp, clinical canvas background
val LightSurface = Color(0xFFFFFFFF)          // Pure white for high-priority cards
val LightSurfaceVariant = Color(0xFFF1F5F9)   // Soft grey for First Aid/Stock section background
val LightBorder = Color(0xFFE2E8F0)           // Muted border lines for clean partitioning

// Active Schedule Gradients (Parsed from the UI timeline)
val TrackCyanLight = Color(0xFF0284C7)        // Core action blue/cyan
val TrackCyanVariant = Color(0xFF22D3EE)      // Highlighting scheduled intervals
val TrackAmberLight = Color(0xFFD97706)       // Impending dose/alert accent

// ==========================================
// Dark Theme Tokens (Deep Medical Navy)
// ==========================================
val DarkBackground = Color(0xFF0F172A)        // Deep navy blue base canvas
val DarkSurface = Color(0xFF1E293B)           // Slate blue for active container surfaces
val DarkSurfaceVariant = Color(0xFF334155)    // Muted grey-blue for stock inventory cards
val DarkBorder = Color(0xFF475569)            // Subtle dark hairline dividers

// Active Schedule Gradients (Dark Mode optimized)
val TrackCyanDark = Color(0xFF38BDF8)         // High-contrast neon cyan for dark backgrounds
val TrackAmberDark = Color(0xFFFB923C)        // Vivid orange/amber for nighttime warnings

// Common Utilities
val TextPrimaryLight = Color(0xFF0F172A)
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextMuted = Color(0xFF64748B)



val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)



val NavyDark = Color(0xFF081426)      // Primary background
val NavyMedium = Color(0xFF1E3A5F)    // Card/Surface base
val NeonCyan = Color(0xFF22D3EE)      // Primary interactive accent
val NeonOrange = Color(0xFFFB923C)    // Alert/Expiry accent

// Glassmorphism overlays
val GlassWhite = Color(0x1a000000)   // 10% White for frosted effect
val GlassCyan = Color(0x1A22D3EE)    // 10% Cyan for tinted glass