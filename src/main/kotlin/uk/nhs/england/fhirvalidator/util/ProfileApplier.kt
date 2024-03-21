package uk.nhs.england.fhirvalidator.util

import org.hl7.fhir.instance.model.api.IBaseResource
import org.hl7.fhir.r4.model.Bundle

fun getResourcesOfType(resource: IBaseResource, resourceType: String?): List<IBaseResource> {
    val matchingResources = mutableListOf<IBaseResource>()
    // KGM exclude bundles from resource checks
    if (resource.fhirType() == resourceType && !(resource is Bundle)) {
        matchingResources.add(resource)
    }
    if (resource is Bundle) {
       val bundleEntries = resource.entry.map { it }
        if (bundleEntries.any { it.resource is IBaseResource }) {
            resource.entry.stream()
                .map { it.resource }
                .filter { it.fhirType() == resourceType }
                .forEach { matchingResources.add(it) }
        }
    }
    return matchingResources
}

fun applyProfile(resources: List<IBaseResource>, profile: String) {
    resources.stream().forEach {
        if (it.meta.profile.count() === 0) {
            var found = false
            it.meta.profile.forEach {
                if (it.value.equals(profile)) found = true
            }
            if (!found) {
                it.meta.addProfile(profile)
            }
        }
    }
}
