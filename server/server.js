const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Habilitar CORS y JSON
app.use(cors());
app.use(express.json());

// Servir archivos estáticos (Web Client)
app.use(express.static(path.join(__dirname, 'public')));

// --- Configuración de Persistencia ---

// Opción A: MongoDB (Nube)
const MONGODB_URI = process.env.MONGODB_URI;
let useMongoDB = false;

// Esquema de Mongoose
const ParkingSchema = new mongoose.Schema({
    location: String,
    user: String,
    timestamp: String,
    timestampRaw: Number
});
const ParkingModel = mongoose.model('Parking', ParkingSchema);

// Opción B: Archivo local (Fallback)
const DATA_FILE = path.join(__dirname, 'parking_data.json');
let parkingStatus = {
    location: "Desconocido",
    user: "",
    timestamp: "",
    timestampRaw: 0
};

// Función para conectar a DB
async function connectDB() {
    if (MONGODB_URI) {
        try {
            await mongoose.connect(MONGODB_URI);
            console.log('✅ Conectado a MongoDB Atlas');
            useMongoDB = true;

            // Cargar estado inicial de DB
            const latest = await ParkingModel.findOne().sort({ timestampRaw: -1 });
            if (latest) {
                parkingStatus = latest.toObject();
                console.log('📂 Estado cargado desde MongoDB:', parkingStatus);
            }
        } catch (err) {
            console.error('❌ Error conectando a MongoDB:', err);
            console.log('⚠️ Usando sistema de archivos local como fallback');
        }
    } else {
        console.log('ℹ️ No se detectó MONGODB_URI. Usando sistema de archivos local.');
    }

    if (!useMongoDB) {
        loadStatusFromFile();
    }
}

// Funciones de Archivo Local (Legacy/Fallback)
function loadStatusFromFile() {
    try {
        if (fs.existsSync(DATA_FILE)) {
            const data = fs.readFileSync(DATA_FILE, 'utf8');
            parkingStatus = JSON.parse(data);
            console.log('📂 Estado cargado desde archivo local:', parkingStatus);
        }
    } catch (err) {
        console.error('❌ Error al cargar archivo local:', err);
    }
}

function saveStatusToFile() {
    try {
        fs.writeFileSync(DATA_FILE, JSON.stringify(parkingStatus, null, 2));
    } catch (err) {
        console.error('❌ Error al guardar en archivo local:', err);
    }
}

// Iniciar conexión
connectDB();

// --- API ---

// GET /status
app.get('/status', async (req, res) => {
    // Si usamos Mongo, intentar obtener el más reciente (por si otro proceso lo actualizó)
    if (useMongoDB) {
        try {
            const latest = await ParkingModel.findOne().sort({ timestampRaw: -1 });
            if (latest) {
                parkingStatus = latest.toObject();
            }
        } catch (err) {
            console.error('Error leyendo DB:', err);
        }
    }
    res.json(parkingStatus);
});

// POST /status
app.post('/status', async (req, res) => {
    const { location, user, timestamp, timestampRaw } = req.body;

    if (!location || !user) {
        return res.status(400).json({ error: 'Faltan datos' });
    }

    parkingStatus = { location, user, timestamp, timestampRaw };

    // Guardar según el método activo
    if (useMongoDB) {
        try {
            // Guardamos un nuevo registro (histórico) o actualizamos el único documento?
            // Para simplificar, guardamos nuevo registro. Así tenemos historial.
            const newEntry = new ParkingModel(parkingStatus);
            await newEntry.save();
            console.log('💾 Guardado en MongoDB');

            // Opcional: Borrar antiguos para no llenar la DB gratis
            // await ParkingModel.deleteMany({ timestampRaw: { $lt: timestampRaw } });
        } catch (err) {
            console.error('❌ Error guardando en MongoDB:', err);
            return res.status(500).json({ error: 'Error de base de datos' });
        }
    } else {
        saveStatusToFile();
        console.log('💾 Guardado en archivo local');
    }

    console.log(`🚗 Nueva ubicación: ${location} (${user})`);
    res.json({ success: true, data: parkingStatus });
});

// GET / (Sirve la app web)
// Express sirve 'index.html' automáticamente desde /public, pero podemos dejar esto explícito si falla
// app.get('/', (req, res) => { res.sendFile(path.join(__dirname, 'public', 'index.html')); });

app.listen(PORT, () => {
    console.log(`🚀 Servidor corriendo en puerto ${PORT}`);
});
