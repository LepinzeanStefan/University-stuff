#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "structs.h"
#include "labels.h"

#define COMMAND_SIZE 20

// ----------------------------------------------------------------------------------------

/* 
 * Citeste datele din fisierul binar si le memoreaza intr-un vector
 * alocat dinamic in corespondenta cu cerinta temei
 */
sensor* allocSensorVector(int size, FILE* fin) {
	sensor* sensor_vec = (sensor *) calloc(size,sizeof(sensor));
	if(sensor_vec == NULL) {
		printf("ERROR : Sensor_vec is NULL");
		return NULL;
	}
	for (int i = 0; i < size; i++) {
		fread(&(sensor_vec[i].sensor_type), sizeof(int), 1, fin);
		if (sensor_vec[i].sensor_type == 1) {
			int s = sizeof(power_management_unit);
			// Retin aici marimea structurii pentru a nu lungi prea mult linia
			power_management_unit* data = (power_management_unit *) calloc(1, s);
			if (data == NULL) {
				printf("ERROR : Something went wrong");
				return NULL;
			}
			fread(&(data->voltage), sizeof(float), 1, fin);
			fread(&(data->current), sizeof(float), 1, fin);
			fread(&(data->power_consumption), sizeof(float), 1, fin);
			fread(&(data->energy_regen), sizeof(int), 1, fin);
			fread(&(data->energy_storage), sizeof(float), 1, fin);
			sensor_vec[i].sensor_data = data;
			fread(&(sensor_vec[i].nr_operations), sizeof(int), 1, fin);
			sensor_vec[i].operations_idxs = calloc(sensor_vec[i].nr_operations, sizeof(int));
			fread(sensor_vec[i].operations_idxs, sizeof(int), sensor_vec[i].nr_operations, fin);
		}
		else {
			int s = sizeof(tire_sensor);
			tire_sensor* data = (tire_sensor *) calloc(1,s);
			if (data == NULL) {
				printf("ERROR : Something went wrong");
				return NULL;
			}
			fread(&(data->pressure), sizeof(float), 1, fin);
			fread(&(data->temperature), sizeof(float), 1, fin);
			fread(&(data->wear_level), sizeof(int), 1, fin);
			fread(&(data->performace_score), sizeof(int), 1, fin);
			sensor_vec[i].sensor_data = data;
			fread(&(sensor_vec[i].nr_operations), sizeof(int), 1, fin);
			sensor_vec[i].operations_idxs = calloc(sensor_vec[i].nr_operations, sizeof(int));
			fread(sensor_vec[i].operations_idxs, sizeof(int), sensor_vec[i].nr_operations, fin);
		}
	}
	return sensor_vec;
}
// ----------------------------------------------------------------------------------------

/*
 * Dezaloca vectorul de senzori impreuna cu datele stocate in el si intoarce NULL
 */
sensor* freeSensorVector(sensor* sensor_vec, int size) {
	for (int i = 0; i < size; i++) {
		if (sensor_vec[i].sensor_type == 1) {
			power_management_unit* data = sensor_vec[i].sensor_data;
			free(data);
			sensor_vec[i].sensor_data = NULL;
		}
		else {
			tire_sensor* data = sensor_vec[i].sensor_data;
			free(data);
			sensor_vec[i].sensor_data = NULL;
		}
		free(sensor_vec[i].operations_idxs);
	}
	free(sensor_vec);
	return NULL;
}
// ----------------------------------------------------------------------------------------

/*
 * Functia de print care va afisa vectorul de senzori
 */
void printCommand(sensor* sensor_vec, int index, int size) {
	if (index < 0 || index > size - 1) {
		printf("Index not in range!\n");
		return;
	}
	if (sensor_vec[index].sensor_type == 1) {
		power_management_unit* data = sensor_vec[index].sensor_data;
		// Folosim un pointer pentru a manipula datele mai usor si a facilita citirea codului
		printf("Power Management Unit\n");
		printf("Voltage: %.2f\n", data->voltage);
		printf("Current: %.2f\n", data->current);
		printf("Power Consumption: %.2f\n", data->power_consumption);
		printf("Energy Regen: %d%%\n", data->energy_regen);
		printf("Energy Storage: %d%%\n", data->energy_storage);
	}
	else {
		tire_sensor* data = sensor_vec[index].sensor_data;
		printf("Tire Sensor\n");
		printf("Pressure: %.2f\n", data->pressure);
		printf("Temperature: %.2f\n", data->temperature);
		printf("Wear Level: %d%%\n", data->wear_level);
		if (data->performace_score == 0) {
			printf("Performance Score: Not Calculated\n");
		}
		else {
			printf("Performance Score: %d\n", data->performace_score);
		}
	}
}
// ----------------------------------------------------------------------------------------

/*
 * Analizeaza functiile aflate in senzorul ales, comenzile sunt apelate direct din
 * vectorul de comenzi
 */
void analyzeCommand(sensor* sensor_vec, int index, int size, void (**operations)(void*)) {
	if (index < 0 || index > size - 1) {
		printf("Index not in range!\n");
		return;
	}
	for (int i = 0; i < sensor_vec[index].nr_operations; i++)
	{
		int operation_index = sensor_vec[index].operations_idxs[i];
		operations[operation_index](sensor_vec[index].sensor_data);
	}
}
// ----------------------------------------------------------------------------------------

/*
 * Functia de compare care va fi data drept argument pentru qsort, va pune la
 * stanga senzorii de tip pmu si la dreapta cei de tip tire
 */
int cmpSensor(const void* a, const void* b) {
	return ( (*(sensor *)b).sensor_type - (*(sensor *)a).sensor_type );
}
// ----------------------------------------------------------------------------------------

/*
 * Sterge senzorii defecti si realoca vectorul de senzori pentru a scapa de spatiul
 * nefolosit. Realocare se va face utilizand un vector secundar.
 */
sensor* clearFaultySensors(sensor* sensor_vec, int* size) {
	int i = 0, j = 0, newsize = *size;
	// initializam un vector care va retine pozitiile sensorilor functionali
	int pos_vec[newsize];
	for (; i < *size; i++) {
		// verificam, pe rand, daca fiecare valoare din sensor_data este valida
		if (sensor_vec[i].sensor_type == 1) {
			int ok = 1;
			power_management_unit* data = sensor_vec[i].sensor_data;
			if (data->voltage < 10 || data->voltage > 20) {
				ok = 0;
			}
			if (data->current < -100 || data->current > 100) {
				ok = 0;
			}
			if (data->power_consumption < 0 || data->power_consumption > 1000) {
				ok = 0;
			}
			if (data->energy_regen < 0 || data->energy_regen > 100) {
				ok = 0;
			}
			if (data->energy_storage < 0 || data->energy_storage > 100) {
				ok = 0;
			}
			if (ok) {
				// toti senzorii sunt functionali
				pos_vec[j++] = i; // pozitia senzorului este adaugata in vector
			}
			else {
				// cel putin un senzor este defect, il eliminam
				free(sensor_vec[i].sensor_data);
				free(sensor_vec[i].operations_idxs);
				// este necesar sa eliminam doar datele alocate dinamic din senzor
				newsize--;
			}
		}
		else {
			int ok = 1;
			tire_sensor* data = sensor_vec[i].sensor_data;
			if (data->pressure < 19 || data->pressure > 28) {
				ok = 0;
			}
			if (data->temperature < 0 || data->temperature > 120) {
				ok = 0;
			}
			if (data->wear_level < 0 || data->wear_level > 100) {
				ok = 0;
			}
			if (ok) {
				// toti senzorii sunt functionali
				pos_vec[j++] = i;// pozitia senzorului este adaugata in vector
			}
			else {
				// cel putin un senzor este defect, il eliminam
				free(sensor_vec[i].sensor_data);
				free(sensor_vec[i].operations_idxs);
				// este necesar sa eliminam doar datele alocate dinamic in senzor
				newsize--;
			}
		}
	}
	sensor* new_sensor_vec = (sensor *) calloc(newsize, sizeof(sensor));
	// initializam un nou vector care va retine doar senzorii valizi, cu aceeasi precedenta
	if (new_sensor_vec == NULL) {
		printf("ERROR: The new sensor couldn't be allocated");
		exit(-1);
	}
	for (i = 0; i < newsize; i++) {
		new_sensor_vec[i] = sensor_vec[pos_vec[i]];
	}
	free(sensor_vec); // dezalocam memoria vectorului vechi
	*size = newsize; // schimbam valoarea lui "size" pentru vectorul nou
	return new_sensor_vec;
}
// ----------------------------------------------------------------------------------------

int main(int argc, char const *argv[])
{
	FILE* fin = fopen(argv[1], "rb");
	if (fin == NULL) {
		printf("ERROR: Fisierul de input nu a fost deschis corect");
		return -1;
	}
	int size; // Aceasta va fi marimea vectorului de senzori
	fread(&size, sizeof(int), 1, fin);
	// Vectorul de operatii
	void (*operations[8])(void*);
	get_operations(operations);

	sensor* sensor_vec = allocSensorVector(size, fin);
	// Acesta este vectorul de senzori care va fi ordonat folosind qsort
	qsort(sensor_vec, size, sizeof(sensor), cmpSensor);

	char command[COMMAND_SIZE]; // Sir de caractere care va retine commanda data de input
	int index; // Integer care va retine indexul comenzii
	scanf("%s", command);
	while (strcmp(command, "exit")) {
		if (strcmp(command, "print") == 0) {
			scanf("%d", &index);
			printCommand(sensor_vec, index, size);
		}
		if (strcmp(command, "analyze") == 0) {
			scanf("%d", &index);
			analyzeCommand(sensor_vec, index, size, operations);
		}
		if (strcmp(command, "clear") == 0) {
			sensor_vec = clearFaultySensors(sensor_vec, &size);
		}
		scanf("%s", command);
	}

	sensor_vec = freeSensorVector(sensor_vec, size);
	fclose(fin);
	return 0;
}
