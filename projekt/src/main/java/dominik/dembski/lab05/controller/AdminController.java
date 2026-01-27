package dominik.dembski.lab05.controller;

import dominik.dembski.lab05.domain.Cpu;
import dominik.dembski.lab05.domain.CpuSpecification;
import dominik.dembski.lab05.domain.Manufacturer;
import dominik.dembski.lab05.domain.Technology;
import dominik.dembski.lab05.dto.ManufacturerStatsDTO;
import dominik.dembski.lab05.service.CpuService;
import dominik.dembski.lab05.service.CpuSpecificationService;
import dominik.dembski.lab05.service.ManufacturerService;
import dominik.dembski.lab05.service.TechnologyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Kontroler Spring MVC dla panelu Administratora.
 * Obsługuje widoki Thymeleaf dla zarządzania CPU.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CpuService cpuService;
    private final ManufacturerService manufacturerService;
    private final TechnologyService technologyService;
    private final CpuSpecificationService cpuSpecificationService;

    public AdminController(CpuService cpuService, 
                          ManufacturerService manufacturerService,
                          TechnologyService technologyService,
                          CpuSpecificationService cpuSpecificationService) {
        this.cpuService = cpuService;
        this.manufacturerService = manufacturerService;
        this.technologyService = technologyService;
        this.cpuSpecificationService = cpuSpecificationService;
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    /**
     * Strona główna panelu administratora z podsumowaniem.
     */
    @GetMapping
    public String dashboard(Model model) {
        List<Cpu> cpus = cpuService.getAllCpus();
        List<Manufacturer> manufacturers = manufacturerService.getAllManufacturers();
        List<Technology> technologies = technologyService.getAllTechnologies();
        List<ManufacturerStatsDTO> stats = cpuService.getManufacturerPerformanceStats();

        model.addAttribute("cpuCount", cpus.size());
        model.addAttribute("manufacturerCount", manufacturers.size());
        model.addAttribute("technologyCount", technologies.size());
        model.addAttribute("manufacturerStats", stats);
        model.addAttribute("recentCpus", cpus.stream().limit(5).toList());

        return "admin/dashboard";
    }

    // =====================================================
    // ZARZĄDZANIE CPU - CRUD
    // =====================================================

    /**
     * Lista wszystkich CPU.
     */
    @GetMapping("/cpus")
    public String listCpus(Model model) {
        List<Cpu> cpus = cpuService.getAllCpus();
        model.addAttribute("cpus", cpus);
        return "admin/cpu-list";
    }

    /**
     * Formularz dodawania nowego CPU.
     */
    @GetMapping("/cpus/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cpu", new Cpu());
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        model.addAttribute("isEdit", false);
        return "admin/cpu-form";
    }

    /**
     * Obsługa formularza dodawania CPU.
     */
    @PostMapping("/cpus/new")
    public String createCpu(@ModelAttribute Cpu cpu,
                           @RequestParam(required = false) UUID manufacturerId,
                           @RequestParam(required = false) List<UUID> technologyIds,
                           @RequestParam(required = false) Integer cacheL1KB,
                           @RequestParam(required = false) Integer cacheL2KB,
                           @RequestParam(required = false) Integer cacheL3MB,
                           @RequestParam(required = false) Integer tdpWatts,
                           @RequestParam(required = false) String socketType,
                           RedirectAttributes redirectAttributes) {
        try {
            // Utwórz specyfikację jeśli podane są jakiekolwiek pola
            if (cacheL1KB != null || cacheL2KB != null || cacheL3MB != null || tdpWatts != null || socketType != null) {
                CpuSpecification specification = new CpuSpecification(
                    cacheL1KB != null ? cacheL1KB : 0,
                    cacheL2KB != null ? cacheL2KB : 0,
                    cacheL3MB != null ? cacheL3MB : 0,
                    tdpWatts != null ? tdpWatts : 0,
                    socketType
                );
                CpuSpecification savedSpec = cpuSpecificationService.addSpecification(specification);
                cpu.setSpecification(savedSpec);
            }

            if (manufacturerId != null) {
                cpuService.createCpuWithManufacturerAndTechnologies(cpu, manufacturerId, technologyIds);
            } else {
                cpuService.addCpu(cpu);
            }
            redirectAttributes.addFlashAttribute("successMessage", "CPU zostało pomyślnie dodane!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas dodawania CPU: " + e.getMessage());
        }
        return "redirect:/admin/cpus";
    }

    /**
     * Formularz edycji CPU.
     */
    @GetMapping("/cpus/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cpu> cpuOpt = cpuService.getCpuById(id);
        if (cpuOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "CPU o podanym ID nie istnieje!");
            return "redirect:/admin/cpus";
        }

        model.addAttribute("cpu", cpuOpt.get());
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        model.addAttribute("isEdit", true);
        return "admin/cpu-form";
    }

    /**
     * Obsługa formularza edycji CPU.
     */
    @PostMapping("/cpus/edit/{id}")
    public String updateCpu(@PathVariable UUID id,
                           @ModelAttribute Cpu cpu,
                           @RequestParam(required = false) UUID manufacturerId,
                           @RequestParam(required = false) List<UUID> technologyIds,
                           @RequestParam(required = false) Integer cacheL1KB,
                           @RequestParam(required = false) Integer cacheL2KB,
                           @RequestParam(required = false) Integer cacheL3MB,
                           @RequestParam(required = false) Integer tdpWatts,
                           @RequestParam(required = false) String socketType,
                           RedirectAttributes redirectAttributes) {
        try {
            Cpu updated = cpuService.updateCpu(id, cpu);
            if (updated != null) {
                // Aktualizuj specyfikację
                if (cacheL1KB != null || cacheL2KB != null || cacheL3MB != null || tdpWatts != null || socketType != null) {
                    CpuSpecification specification;
                    if (updated.getSpecification() != null) {
                        specification = updated.getSpecification();
                        if (cacheL1KB != null) specification.setCacheL1KB(cacheL1KB);
                        if (cacheL2KB != null) specification.setCacheL2KB(cacheL2KB);
                        if (cacheL3MB != null) specification.setCacheL3MB(cacheL3MB);
                        if (tdpWatts != null) specification.setTdpWatts(tdpWatts);
                        if (socketType != null) specification.setSocketType(socketType);
                    } else {
                        specification = new CpuSpecification(
                            cacheL1KB != null ? cacheL1KB : 0,
                            cacheL2KB != null ? cacheL2KB : 0,
                            cacheL3MB != null ? cacheL3MB : 0,
                            tdpWatts != null ? tdpWatts : 0,
                            socketType
                        );
                    }
                    CpuSpecification savedSpec = cpuSpecificationService.addSpecification(specification);
                    updated.setSpecification(savedSpec);
                    cpuService.addCpu(updated);
                }

                // Aktualizuj producenta jeśli został wybrany
                if (manufacturerId != null) {
                    cpuService.assignManufacturerToCpu(id, manufacturerId);
                }
                // Aktualizuj technologie jeśli zostały wybrane
                if (technologyIds != null && !technologyIds.isEmpty()) {
                    cpuService.assignTechnologiesToCpu(id, technologyIds);
                }
                redirectAttributes.addFlashAttribute("successMessage", "CPU zostało pomyślnie zaktualizowane!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "CPU o podanym ID nie istnieje!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas aktualizacji CPU: " + e.getMessage());
        }
        return "redirect:/admin/cpus";
    }

    /**
     * Usuwanie CPU.
     */
    @PostMapping("/cpus/delete/{id}")
    public String deleteCpu(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            cpuService.deleteCpuById(id);
            redirectAttributes.addFlashAttribute("successMessage", "CPU zostało pomyślnie usunięte!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas usuwania CPU: " + e.getMessage());
        }
        return "redirect:/admin/cpus";
    }

    /**
     * Szczegóły CPU.
     */
    @GetMapping("/cpus/{id}")
    public String showCpuDetails(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cpu> cpuOpt = cpuService.getCpuById(id);
        if (cpuOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "CPU o podanym ID nie istnieje!");
            return "redirect:/admin/cpus";
        }

        model.addAttribute("cpu", cpuOpt.get());
        return "admin/cpu-details";
    }

    // =====================================================
    // ZARZĄDZANIE PRODUCENTAMI
    // =====================================================

    @GetMapping("/manufacturers")
    public String listManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        return "admin/manufacturer-list";
    }

    @GetMapping("/manufacturers/new")
    public String showManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("isEdit", false);
        return "admin/manufacturer-form";
    }

    @PostMapping("/manufacturers/new")
    public String createManufacturer(@ModelAttribute Manufacturer manufacturer,
                                    RedirectAttributes redirectAttributes) {
        try {
            manufacturerService.addManufacturer(manufacturer);
            redirectAttributes.addFlashAttribute("successMessage", "Producent został pomyślnie dodany!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/manufacturers";
    }

    @PostMapping("/manufacturers/delete/{id}")
    public String deleteManufacturer(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            manufacturerService.deleteManufacturerById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producent został usunięty!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/manufacturers";
    }

    // =====================================================
    // ZARZĄDZANIE TECHNOLOGIAMI
    // =====================================================

    @GetMapping("/technologies")
    public String listTechnologies(Model model) {
        model.addAttribute("technologies", technologyService.getAllTechnologies());
        return "admin/technology-list";
    }

    @GetMapping("/technologies/new")
    public String showTechnologyForm(Model model) {
        model.addAttribute("technology", new Technology());
        model.addAttribute("isEdit", false);
        return "admin/technology-form";
    }

    @PostMapping("/technologies/new")
    public String createTechnology(@ModelAttribute Technology technology,
                                  RedirectAttributes redirectAttributes) {
        try {
            technologyService.addTechnology(technology);
            redirectAttributes.addFlashAttribute("successMessage", "Technologia została pomyślnie dodana!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/technologies";
    }

    @PostMapping("/technologies/delete/{id}")
    public String deleteTechnology(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            technologyService.deleteTechnologyById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Technologia została usunięta!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/technologies";
    }
}
