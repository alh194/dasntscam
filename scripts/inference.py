import easyocr
import sys
import re
import os
import pandas as pd
import json

# Rutas basadas en la ubicación de este script
BASE       = os.path.dirname(os.path.abspath(__file__))       # .../scripts
MODEL_DIR  = os.path.abspath(os.path.join(BASE, '..', 'model'))
CSV_PATH   = os.path.abspath(os.path.join(BASE, '..', 'data', 'padron_vados.csv'))

# 1) Creamos el lector, PERMITIENDO descargas solo esta vez
reader = easyocr.Reader(
    ['es'],
    detect_network='craft',
    gpu=False,
    model_storage_directory=MODEL_DIR,
    download_enabled=True,   # <- ¡IMPORTANTE! deja que baje los .pth
    verbose=False            # <- silencia sólo la barra de progreso
)

# 2) Cargamos la imagen y extraemos texto
image_path = sys.argv[1]
result = reader.readtext(image_path)

# 3) Extraemos la placa (4 ó 5 dígitos)
placa_number = None
for box, text, conf in result:
    if re.fullmatch(r'\d{4,5}', text):
        placa_number = text
        break

if not placa_number:
    # Imprimimos un mensaje de error a STDERR
    print("ERROR: placa no detectada", file=sys.stderr)
    sys.exit(1)

# 4) Leemos el CSV correctamente
try:
    df = pd.read_csv(CSV_PATH, dtype=str)
except Exception as e:
    print(f"ERROR: no pude leer {CSV_PATH}: {e}", file=sys.stderr)
    sys.exit(1)

# 5) Buscamos la fila y devolvemos JSON
match = df[df['placa'] == placa_number]
if not match.empty:
    row = match.iloc[0]
    out = {
        "direccion": row['direccion'],
        "placa":      row['placa'],
        "vigente":    row['vigente']
    }
    print(json.dumps(out, ensure_ascii=False))
    sys.exit(0)
else:
    print(f"ERROR: placa {placa_number} no encontrada en CSV", file=sys.stderr)
    sys.exit(1)
